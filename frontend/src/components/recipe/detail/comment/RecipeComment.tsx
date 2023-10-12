import React, { useState, useEffect, useRef, useCallback } from 'react'
import * as S from '../../../../styles/recipe/detail/comment/RecipeComment.styled'
import axiosInstance from '../../../../utils/fetchCall'
import { CommentItem } from '../../../../constants/Interfacs'
import CommonButton from '../../../ui/Button'

function RecipeComment() {
  const [commentValue, setCommentValue] = useState('')
  const [commentList, setCommentList] = useState<CommentItem[]>([])
  const [pageIdx, setPageIdx] = useState(1)
  const pageSize = useRef(5)
  const endOfList = useRef(false)

  const handleClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    setCommentValue(event.currentTarget.value)
  }

  const onSubmit = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    const body = {
      message: commentValue,
      recipeId: 1,
    }
    const setComment = async () => {
      const res = await axiosInstance.post('/comment/1', body)
      if (res.status === 200) {
        setCommentValue('')
        setPageIdx(1)
      }
    }
    setComment()
  }

  const getComment = useCallback(async () => {
    const res = await axiosInstance.get(
      `/comment/10/${pageIdx}/${pageSize.current}`
    )
    if (res.status === 200) {
      const comment = res.data.commentDtoList

      const messages = comment.map((commentItem: CommentItem) => {
        const parsedComment = JSON.parse(commentItem.message)
        return parsedComment || commentItem
      })

      if (messages.length === 0) {
        endOfList.current = true
      } else if (pageIdx === 1) {
        setCommentList(messages)
      } else {
        setCommentList(prevCommentList => [...prevCommentList, ...messages])
      }
    }
  }, [pageIdx])

  useEffect(() => {
    getComment()
  }, [getComment])

  const handleLoadMoreComments = () => {
    if (!endOfList.current) {
      setPageIdx(prevPageIdx => prevPageIdx + 1)
    }
  }

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
        <S.CommentSubmit onClick={onSubmit}>등록</S.CommentSubmit>
      </S.CommentForm>

      <S.CommentList>
        {commentList.map((commentItem, index) => (
          <div key={commentItem.commentId || index}>
            <S.CommentItemWrapper>
              <S.CommentHeader>
                <S.CommentUserInfo>
                  <S.UserImg
                    src={commentItem.profileUrl}
                    alt={commentItem.nickname}
                  />
                  <S.UserNickname>{commentItem.nickname}</S.UserNickname>
                </S.CommentUserInfo>
                <S.CommentDate>{commentItem.createdAt}</S.CommentDate>
              </S.CommentHeader>

              <S.CommentContent>{commentItem.message}</S.CommentContent>
              <S.CommentButtonWrap>
                <CommonButton size="small" color="divider">
                  수정
                </CommonButton>
                <CommonButton size="small" color="divider">
                  삭제
                </CommonButton>
              </S.CommentButtonWrap>
            </S.CommentItemWrapper>
          </div>
        ))}
      </S.CommentList>

      <S.MoreButtonBox>
        <CommonButton
          onClick={handleLoadMoreComments}
          color="main"
          border="border"
        >
          댓글 더보기
        </CommonButton>
      </S.MoreButtonBox>
    </S.CommentContainer>
  )
}

export default RecipeComment
