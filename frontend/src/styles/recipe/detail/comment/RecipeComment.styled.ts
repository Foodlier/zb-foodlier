import styled from 'styled-components'
import { palette } from '../../../../constants/Styles'

export const CommentContainer = styled.section`
  padding-bottom: 15rem;
`
// export const RecipeCommentList = styled.section`
//   padding-bottom: 15rem;
// `

export const CommentTit = styled.h2`
  margin-bottom: 2rem;
`
export const CommentList = styled.section`
  background-color: #f2f2f2;
`
// export const LoadMoreCommentsButton = styled.div`
//   margin-bottom: 2rem;
// `
// export const MoreButtonBox = styled.div`
//   display: flex;
//   justify-content: center;
//   margin-top: 5rem;
// `
export const CommentUserInfo = styled.div`
  display: flex;
  align-items: center;
`

export const UserImg = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 50%;
`

export const UserNickname = styled.p`
  padding-left: 1rem;
`

export const CommentDate = styled.p`
  display: flex;
  align-items: center;
  color: ${palette.textSecondary};
`
export const CommentContent = styled.div`
  width: 100%;
`

// RecipeCommentForm
export const CommentForm = styled.form`
  width: 100%;
  display: flex;

  background: ${palette.white};
  border-radius: 1rem;
  height: 7rem;
`

export const CommentInput = styled.input`
  flex: 1 1 auto;
  padding-left: 2rem;
  border-radius: 1rem 0 0 1rem;
  border: 0.1rem solid ${palette.divider};
  border-right: 0.1rem solid ${palette.white};
`

export const CommentSubmit = styled.button`
  background-color: ${palette.main};
  border-radius: 0 1rem 1rem 0;
  width: 20%;
  max-width: 10rem;
  color: ${palette.white};
  font-weight: 600;
  font-size: 1.6rem;
`
