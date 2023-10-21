import styled from 'styled-components'
import { breakpoints, palette, weight } from '../../constants/Styles'

export const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 5%;
  width: 100%;
  margin: 0 auto;
`

export const RequestHeader = styled.h2`
  display: block;
  width: 100%;
  text-align: center;
  padding-bottom: 1%;
  margin-bottom: 3%;
  font-size: 2.4rem;
  border-bottom: 1px solid ${palette.divider};
`

export const RequestForm = styled.form`
  padding-top: 4%;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`

export const RequestFormList = styled.ul`
  width: 90%;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding-left: 0;
`
export const RequestFormEl = styled.li`
  padding-bottom: 10px;
  border-bottom: 1px solid ${palette.divider};
  &:last-of-type {
    border-bottom: none;
  }
`

export const ElementTitle = styled.p`
  font-weight: 600;
  margin-bottom: 10px;
  color: ${palette.textSecondary};
`
export const ElementContents = styled.span`
  display: inline-block;
  margin-right: 10px;
  font-size: 20px;
  font-weight: ${weight.content};
`

export const IngredientsBoxContainer = styled.div`
  display: flex;
  gap: 20px;
`

export const IngredientsBox = styled.div`
  padding: 10px 14px;
  border: 1px solid ${palette.divider};
  border-radius: 10px;
`

export const TaggedRecipe = styled.div`
  width: 200px;
  height: 240px;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
`

export const TaggedImg = styled.img`
  width: 100%;
  height: 50%;
  background-color: teal;
  border-radius: 10px 10px 0 0;
`

export const TaggedInfo = styled.div`
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 10px;
`

export const TaggedTopInfo = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`
export const TaggedTitle = styled.span`
  font-weight: bold;
`

export const TaggedLike = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  span {
    font-size: 1.2rem;
  }
`

export const TaggedBottomInfo = styled.p`
  font-size: 1.4rem;
`

export const ButtonList = styled.div`
  margin: 50px 0;
  display: flex;
  justify-content: center;
  gap: 10px;

  ${breakpoints.large} {
    margin: 50px 0;
  }
`

export const RejectButton = styled.button`
  width: 120px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background-color: ${palette.white};
  color: ${palette.main};
  border: 1px solid ${palette.main};
  font-weight: 600;
  border-radius: 5px;
`

export const AcceptButton = styled.button<{ $isActive: boolean }>`
  width: 120px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background-color: ${props =>
    props.$isActive ? palette.main : palette.divider};
  color: ${props =>
    props.$isActive ? palette.white : palette.textDisablePlace};

  font-weight: 600;
  border-radius: 5px;
`
export const SpacingDiv = styled.div`
  width: 100%;
  height: 110px;
  ${breakpoints.large} {
    height: 20px;
  }
`

export const WrapQuotation = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
`

export const QuotationText = styled.span``

export const QuotationButton = styled.button`
  background-color: red;
`

export const ErrorMessage = styled.span`
  margin-top: 1rem;
  color: ${palette.main};
  font-size: 1.4rem;
  font-weight: 800;
`
