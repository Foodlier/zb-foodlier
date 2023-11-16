import React, { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/FetchCall'
import CommonButton from '../../../ui/Button'
import ModalWithTwoButton from '../../../ui/ModalWithTwoButton'
import { CommentItem } from '../../../../constants/Interfaces'
import defaultProfile from '../../../../../public/images/default_profile.png'

const RecipeComment = ({
  recipeId,
  activeModal,
}: {
  recipeId: number
  activeModal: () => void
}) => {
  const [commentId, setCommentId] = useState<number | null>(null)
  const [pageIdx, setPageIdx] = useState(0)
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  const [isEditing, setIsEditing] = useState(false)
  const [hasNextCommentPage, setHasNextCommentPage] = useState(true)
  const [commentValue, setCommentValue] = useState('')
  const [editValue, setEditValue] = useState('')
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false)
  const [willModifyCommentId, setWillModifyCommentId] = useState(0)
  const [memberId, setMemberId] = useState(0)
  const navigate = useNavigate()

  const TOKEN: string | null = JSON.parse(
    localStorage.getItem('accessToken') ?? 'null'
  )

  const getProfile = async () => {
    const res = await axiosInstance.get(`/api/profile/private`)

    if (res.status === 200) {
      const commentData = res
      const fetchedMemberId = commentData.data.myMemberId
      setMemberId(fetchedMemberId)
    }
  }

  const getComment = async (idx: number) => {
    const pageSize = 5
    const res = await axiosInstance.get(
      `/api/comment/${recipeId}/${idx}/${pageSize}`
    )
    console.log('끝인지 확인하는거 ::: ', res)

    if (res.status === 200) {
      const commentData = res.data.content
      setHasNextCommentPage(res.data.hasNextPage)

      if (idx === 0) {
        setCommentList(commentData)
      } else {
        setCommentList(prevCommentList => [...prevCommentList, ...commentData])
      }
    }
  }

  // 댓글 더보기
  const handleLoadMoreComments = () => {
    setPageIdx(pageIdx + 1)
    getComment(pageIdx + 1)
  }

  const onSubmit = async (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    try {
      const res = await axiosInstance.post(
        `/api/comment/${recipeId}?message=${commentValue}`
      )
      if (res.status === 200) {
        getComment(0)
        setCommentValue('')
      }
    } catch (error) {
      console.error('새 댓글 등록 중 오류 발생:', error)
    }
  }

  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentValue(event.currentTarget.value)
  }

  // 수정 버튼 누르면
  const handleEditComment = (
    commentIdParams: number, // 수정할 댓글의 고유 식별자
    modifiedMessageParams: string // 수정할 메시지 내용인 문자열
  ) => {
    setIsEditing(true) // 댓글 수정 모드에 들어갔음
    setEditValue(modifiedMessageParams) // 수정할 댓글의 내용을 저장
    setWillModifyCommentId(commentIdParams) // 나중에 댓글을 실제로 수정할 때 어떤 댓글을 수정할 것인지 나타내는데 사용
    setCommentId(commentIdParams) // commentId 상태를 commentIdParams 값으로 설정
    console.log('수정버튼누르면', modifiedMessageParams, commentIdParams)
  }

  // 댓글 수정 취소
  const handleCancelEdit = () => {
    setIsEditing(false)
    setEditValue('')
  }

  // 삭제 버튼 누름
  const handleDeleteComment = (commentIdParams: number) => {
    setIsDeleteModalOpen(true)
    setCommentId(commentIdParams)
  }

  // 삭제 모달 확인 버튼
  const handleConfirmDelete = async () => {
    const res = await axiosInstance.delete(
      `/api/comment/${recipeId}/${commentId}`
    )
    try {
      if (res.status === 200) {
        console.log('삭제성공?!')
        window.location.reload()
      }
    } catch (error) {
      console.error('삭제 안되서 에러남', error)
    } finally {
      setIsDeleteModalOpen(false)
    }
  }

  // 수정 확인 버튼 클릭 시
  const handleEditConfirmClick = async () => {
    try {
      const res = await axiosInstance.put(
        `/api/comment/${commentId}?modifiedMessage=${editValue}`
      )
      if (res.status === 200) {
        setIsEditing(false)
        setEditValue('')
      }
    } catch (error) {
      console.error('댓글 업데이트 실패', error)
    }
  }

  useEffect(() => {
    getProfile()
    getComment(0)
  }, [])

  return (
    <S.CommentContainer>
      <S.CommentTit>댓글</S.CommentTit>
      <S.CommentForm>
        <S.CommentInput
          type="text"
          placeholder="댓글을 입력하세요"
          onChange={handleClick}
          value={commentValue}
        />
        <S.CommentSubmit
          onClick={e => {
            if (TOKEN) {
              onSubmit(e)
            } else {
              activeModal()
            }
          }}
        >
          등록
        </S.CommentSubmit>
      </S.CommentForm>
      <S.CommentList>
        {commentList.map((commentItem, idx) => (
          <S.CommentItemWrapper key={commentItem.id && idx}>
            <S.CommentHeader>
              <S.CommentUserInfo
                onClick={() => navigate(`/profile/${commentItem.memberId}`)}
              >
                <S.UserImg
                  src={commentItem.profileImageUrl || defaultProfile}
                  alt={commentItem.nickname}
                />
                <S.UserNickname>{commentItem.nickname}</S.UserNickname>
              </S.CommentUserInfo>
              <S.CommentDate>
                {commentItem.createdAt.slice(0, 10)}
              </S.CommentDate>
            </S.CommentHeader>
            {isEditing && commentItem.id === willModifyCommentId ? (
              <S.CommentEdit>
                <S.CommentEditInput
                  value={editValue}
                  onChange={e => setEditValue(e.target.value)}
                />
                <S.CommentButtonWrap>
                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={handleEditConfirmClick}
                  >
                    확인
                  </CommonButton>
                  <CommonButton
                    size="small"
                    color="divider"
                    onClick={handleCancelEdit}
                  >
                    취소
                  </CommonButton>
                </S.CommentButtonWrap>
              </S.CommentEdit>
            ) : (
              <>
                <S.CommentContent>{commentItem.message}</S.CommentContent>

                {commentItem.memberId === memberId &&
                commentItem.message !== '삭제된 댓글입니다.' ? (
                  <S.CommentButtonWrap>
                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() =>
                        handleEditComment(commentItem.id, commentItem.message)
                      }
                    >
                      수정
                    </CommonButton>

                    <CommonButton
                      size="small"
                      color="divider"
                      onClick={() => handleDeleteComment(commentItem.id)}
                    >
                      삭제
                    </CommonButton>
                  </S.CommentButtonWrap>
                ) : (
                  <S.CommentButtonWrap />
                )}
              </>
            )}
          </S.CommentItemWrapper>
        ))}
      </S.CommentList>
      <S.MoreButtonBox>
        {commentList.length > 4 && (
          <CommonButton
            onClick={() => handleLoadMoreComments()}
            color={hasNextCommentPage ? 'main' : 'white'}
            border={hasNextCommentPage ? 'border' : 'borderNone'}
          >
            {hasNextCommentPage ? '댓글 더보기' : ''}
          </CommonButton>
        )}
      </S.MoreButtonBox>
      {/* 삭제 Modal */}
      {isDeleteModalOpen && (
        <ModalWithTwoButton
          content="정말 삭제하시겠어요?"
          subContent="샥제 버튼 선택 시, 댓글은 삭제되며 복구되지 않습니다."
          setIsModalFalse={() => setIsDeleteModalOpen(false)}
          modalEvent={handleConfirmDelete}
        />
      )}
    </S.CommentContainer>
  )
}
export default RecipeComment
